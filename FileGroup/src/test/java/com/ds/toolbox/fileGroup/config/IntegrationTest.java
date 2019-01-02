package com.ds.toolbox.fileGroup.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IntegrationTest {

    @Autowired
    public IntegrationConfig.ScanningInputGateway inputGateway;

    @Test
    public void canSeeFilesFromADirectory() {
        inputGateway.startScan("E:\\FTP-IN\\photos\\L_Folder\\ASync_ByDate\\2018\\March 07");
    }
}