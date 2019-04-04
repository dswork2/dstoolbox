import { Component, OnInit } from '@angular/core';
import { RxStompService } from '@stomp/ng2-stompjs';
import { FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-scan',
  templateUrl: './scan.component.html',
  styleUrls: ['./scan.component.scss']
})
export class ScanComponent implements OnInit {
  newScanForm: FormGroup;
  scanPath: FormControl;
  includeSubdirectories: FormControl;
  submitted = false;

  constructor(private rxStompService: RxStompService, private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.newScanForm = this.formBuilder.group({
      scanPath: ['', Validators.required],
      includeSubdirectories: [null],
    });
  }

  onStartScan() {
    this.submitted = true;
    if (this.newScanForm.valid) {
      const message = 'Starting scan at path : ' + JSON.stringify(this.newScanForm.value);
      console.log(message);
      this.rxStompService.publish({
        destination: '/inbound.app/scan.start',
        body: JSON.stringify(this.newScanForm.value)
      });
    }

  }
}
