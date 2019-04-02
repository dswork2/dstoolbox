import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ScanComponent } from './scan/scan.component';
import { FilesComponent } from './files/files.component';

const routes: Routes = [
  {
    path: 'scan',
    component: ScanComponent
  },
  {
    path: 'files',
    component: FilesComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
