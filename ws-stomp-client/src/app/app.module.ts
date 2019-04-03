import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { ScanComponent } from './scan/scan.component';
import { stompConfig } from './stomp-config';
import { InjectableRxStompConfig, RxStompService, rxStompServiceFactory } from '@stomp/ng2-stompjs';
import { FilesComponent } from './files/files.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    ScanComponent,
    FilesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [
    {
    provide: InjectableRxStompConfig,
    useValue: stompConfig,
  },
  {
    provide: RxStompService,
    useFactory: rxStompServiceFactory,
    deps: [InjectableRxStompConfig],
  }
],
  bootstrap: [AppComponent]
})
export class AppModule { }
