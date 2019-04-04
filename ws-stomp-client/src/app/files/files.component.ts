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

  receivedBinaryFiles: Array<any> = [];
// receivedFiles: Array<FileEvent> = [];
  public receivedFiles: any[] = [];
  topicSubscription: Subscription = null;
  public connectionStatus$: Observable<string>;
  public noFilesRecievedMessage = 'No Files received';
  imageToShow: any;

  constructor(private rxStompService: RxStompService) {
    this.connectionStatus$ = this.rxStompService.connectionState$.pipe(map((state) => {
      return RxStompState[state];
    }));
  }

  ngOnInit() {
    this.topicSubscription = this.rxStompService.watch('/publish/scan.result')
    .subscribe((message) => {
      console.log('Received message : '+ JSON.stringify(message));
      this.receivedFiles.push(message);
    });
  }
  ngOnDestroy() {
    this.topicSubscription.unsubscribe();
  }

  createImageFromBlob(image: Blob) {
    const reader = new FileReader();
    reader.addEventListener('load', () => {
      this.imageToShow = reader.result;
      this.receivedBinaryFiles.push(this.imageToShow);
    }, false);

    if (image) {
      reader.readAsBinaryString(image);
    }
   }
}
export interface FileEvent {
  path: string;
  sessionId: string;
}

