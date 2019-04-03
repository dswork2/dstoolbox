import { Component, OnInit } from '@angular/core';
import { RxStompService } from '@stomp/ng2-stompjs';

@Component({
  selector: 'app-scan',
  templateUrl: './scan.component.html',
  styleUrls: ['./scan.component.scss']
})
export class ScanComponent implements OnInit {

  constructor(private rxStompService: RxStompService) { }

  ngOnInit() {
  }
  onStartScan() {
    const message = `Starting scan at path : ${new Date()}`;
    console.log(message);
    this.rxStompService.publish({
      destination: '/inbound.message',
      body: message
    });
  }
}
