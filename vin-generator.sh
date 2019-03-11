#!/bin/bash

VIN_BASE='5J6ZM4H34CL0880'
MAX_NUM_RECORDS_TO_GENERATE=200 #arbitrary max so that data can actually fit in clipboard

CUSTOMER_ID=$1
NUMBER_OF_RECORDS=$2
VIN_TEMPLATE=$3

STARTING_NUMBER=$((1 + RANDOM % 99))
INCREMENTER=""

NUM_COMMAND_ARGS=$#

#========== Pre-Setup Scripts =====================

validate_input () {
  if [ $NUM_COMMAND_ARGS -lt 2 ]; then
    echo "Usage: "
    echo "    ./generate_sql_statements.sh <CUSTOMER_ID> <NUMBER_OF_RECORDS> <SAMPLE_VIN (optional)>"
    echo 
    echo "Example: "
    echo "    ./generate_sql_statements.sh xx00xxx-00xx-x00x-0xxy-abcdefghijkl 25 1F6RM4H34CL088000"
    exit 1
  fi

  if [ $NUMBER_OF_RECORDS -gt $MAX_NUM_RECORDS_TO_GENERATE ]; then
    echo "You can only generate max of ${MAX_NUM_RECORDS_TO_GENERATE} records at once"
    exit 1
  fi
}

# Use requested VIN template if applicable. Format VIN to look like Ford VINS
prep_vin_base () {
  if [ -n "$VIN_TEMPLATE" ]; then
    VIN_BASE=$VIN_TEMPLATE
    validate_vin
  fi
  
  #We're appending a two-digit variation down the road, so just grab the first 15 chars.
  VIN_BASE="1F${VIN_BASE:2:14}"
}

validate_vin () {
  if [ "${#VIN_BASE}" -ne 17 ]; then
    echo "Requested VIN is too short (VINs are normally 17 characters)"
    exit 1
  fi
}

#========== Field Generators =====================

generate_vehicle_name () {
  vehicle_names=("Focus" "Mustang" "F-150 Raptor" "Explorer" "Flex" "Escape" "Expedition")

  name_selection=$((0 + RANDOM % ${#vehicle_names[@]}))

  echo ${vehicle_names[$name_selection]}
}

generate_product_sku () {
  product_skus=("Data Services - Basic 24HR" "Data Services - Enhanced 30S" "Data Services - Premium 30S")

  echo "${product_skus[$((0 + RANDOM % ${#product_skus[@]}))]}"
}

generate_order_id () {
  echo $( hexdump -n 16 -v -e '/1 "%02X"' -e '/16 "\n"' /dev/urandom | sed 's/\(........\)\(....\)\(....\)/\1-\2-\3-/' )
}

#========== SQL Generators =====================

generate_vehicle_inserts () {
  vehicle_name=$(generate_vehicle_name)
  echo "INSERT INTO vehicles (vin, customer_id, name) VALUES ('${VIN_BASE}${INCREMENTER}', '${CUSTOMER_ID}', '${vehicle_name}');"
}

generate_capability_status () {
  status_list=("'SUCCESS', 'IN PROGRESS', null, null" 
      "'SUCCESS', 'SUCCESS', 'IN PROGRESS', null" 
      "'SUCCESS', 'SUCCESS', 'SUCCESS', 'SUCCESS'" 
      "'SUCCESS', 'SUCCESS', 'SUCCESS', 'IN PROGRESS'" 
      "'FAILED', null, null, null" 
      "'SUCCESS', 'CANCELLING', 'SUCCESS', 'SUCCESS'" 
      "'SUCCESS', 'SUCCESS', 'FAILED', null" 
      "'CANCELLED', 'CANCELLED', 'CANCELLED', null" 
      "'SUCCESS', 'CANCEL_FAILED', 'SUCCESS', null")
  
  STATUSES=${status_list[$((0 + RANDOM % ${#status_list[@]}))]}

  order_id=$(generate_order_id)
  product_sku=$(generate_product_sku)

  echo "INSERT INTO vehicle_enrollment_status (vin, order_id, capability_status, subscription_status, provisioning_status, data_is_live_status, customer_id, product_sku) values ('${VIN_BASE}${INCREMENTER}', '${order_id}', ${STATUSES},'${CUSTOMER_ID}','${product_sku}');"
}

#========== The fun starts here =====================

validate_input
prep_vin_base

for (( i=1; i<=$NUMBER_OF_RECORDS; i++ ))
do
  INCREMENTER="$( printf '%02d' "$STARTING_NUMBER" )"
  generate_vehicle_inserts
  generate_capability_status
  STARTING_NUMBER=($((STARTING_NUMBER + 1))  % 100)
done

# SQL Update for Vehicles
# =================================
# with vehicle_updates as (
#    select *, (ABS(CHECKSUM(NewId())) % 7) + 1 as n
#    from vehicles
#    where name like 'DB%' or name is null
# )
# update vehicle_updates
# set name = choose(n, 'Focus','Mustang','F-150 Raptor','Explorer','Flex','Escape','Expedition');

# SQL Update for SKUs
# =================================
# with vehicles_enrollment_updates as (
#    select *, (ABS(CHECKSUM(NewId())) % 3) + 1 as n
#    from vehicle_enrollment_status
#    where product_sku is null
# )
# update vehicles_enrollment_updates
# set product_sku = choose(n, 'Data Services - Basic 24HR','Data Services - Enhanced 30S','Data Services - Premium 30S');

# SQL Update for Order Id
# =================================
# with order_id_updates as (
#    select *, NEWID() as raw_generated_order_id
#    from vehicle_enrollment_status
#    where order_id is null
# )
# update order_id_updates
# set order_id =  CONCAT(
#               SUBSTRING(CAST(NEWID() AS VARCHAR(50)), 1, 23),
#               RIGHT(CAST(NEWID() AS VARCHAR(50)), 12)
#            );