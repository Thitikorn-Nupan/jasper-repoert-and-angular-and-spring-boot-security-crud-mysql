import { Component } from '@angular/core';
import {LoginRequest} from "../../entities/login-request";
import {LoginResponse} from "../../entities/login-response";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  protected loginRequest: LoginRequest = {username : 'test3', password: '1'}
  protected loginResponse!:LoginResponse

  protected onLoginResponseEmitter($event: LoginResponse) {
    this.loginResponse = $event
  }

}
