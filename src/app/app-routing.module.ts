import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./components/home/home.component";
import {ProgramAComponent} from "./components/program-a/program-a.component";
import {ProgramBComponent} from "./components/program-b/program-b.component";
import {ProgramCComponent} from "./components/program-c/program-c.component";

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'program-a', component: ProgramAComponent },
  { path: 'program-b', component: ProgramBComponent },
  { path: 'program-c', component: ProgramCComponent },
  { path: '**', redirectTo: '/', pathMatch: 'full'} // Any request as '' , / will redirect to /options
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
