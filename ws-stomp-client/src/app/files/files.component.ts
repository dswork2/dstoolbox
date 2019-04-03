import { Component, OnInit, OnDestroy } from '@angular/core';
import { RxStompService } from '@stomp/ng2-stompjs';
import { Subscription, Observable } from 'rxjs';
import { RxStompState } from '@stomp/rx-stomp';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-files',
  templateUrl: './files.component.html',
  styleUrls: ['./files.component.scss']
})
export class FilesComponent implements OnInit, OnDestroy {

  public receivedFiles: any[] = [];
  topicSubscription: Subscription = null;
  public connectionStatus$: Observable<string>;
  public noFilesRecievedMessage = 'No Files received';

  constructor(private rxStompService: RxStompService) {
    this.connectionStatus$ = this.rxStompService.connectionState$.pipe(map((state) => {
      return RxStompState[state];
    }));
  }

  ngOnInit() {
    this.topicSubscription = this.rxStompService.watch('/inbound.broker')
    .subscribe((message) => {
      console.log('Received message : ${message}');
      this.receivedFiles.push(message);
    });
  }
  ngOnDestroy() {
    this.topicSubscription.unsubscribe();
  }
}
