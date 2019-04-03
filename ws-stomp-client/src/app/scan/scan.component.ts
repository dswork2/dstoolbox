import { Component, OnInit } from '@angular/core';
import { RxStompService } from '@stomp/ng2-stompjs';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-scan',
  templateUrl: './scan.component.html',
  styleUrls: ['./scan.component.scss']
})
export class ScanComponent implements OnInit {
  scanStartPath = '';
  newScanForm: FormGroup;
  scanPath: FormControl;
  includeSubdirectories: FormControl;

  constructor(private rxStompService: RxStompService) { }

  ngOnInit() {
    this.scanPath = new FormControl('', [Validators.required]);
    this.includeSubdirectories = new FormControl();

    this.newScanForm = new FormGroup({
      scanPath: this.scanPath,
      includeSubdirectories: this.includeSubdirectories,
    });
  }
  onStartScan() {
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
