import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MenubarComponent } from './components/menubar/menubar.component';
import { HomeComponent } from './components/home/home.component';
import { ProgramAComponent } from './components/program-a/program-a.component';
import { ProgramBComponent } from './components/program-b/program-b.component';
import { ProgramCComponent } from './components/program-c/program-c.component';
import { LoginComponent } from './components/home/login/login.component';
import { ProgramComponent } from './components/home/program/program.component';
import {provideHttpClient, withInterceptorsFromDi} from "@angular/common/http";
import {FormsModule} from "@angular/forms";

@NgModule({
  declarations: [
    AppComponent,
    MenubarComponent,
    HomeComponent,
    ProgramAComponent,
    ProgramBComponent,
    ProgramCComponent,
    LoginComponent,
    ProgramComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule
  ],
  providers: [
    // for http client
    provideHttpClient(withInterceptorsFromDi()),
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
