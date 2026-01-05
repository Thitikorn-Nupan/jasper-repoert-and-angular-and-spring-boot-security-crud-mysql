import {  Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {RegisterService} from "../../../services/register.service";
import {LoginRequest} from "../../../entities/login-request";
import {LoginResponse} from "../../../entities/login-response";

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{

  @Input()
  public loginRequest!: LoginRequest;
  @Output()
  public loginResponse : EventEmitter<LoginResponse> = new EventEmitter();
  protected token! : string | null;

  constructor(private readonly registerService:RegisterService) {}

  ngOnInit(): void {
    this.token = sessionStorage.getItem('token')
    setTimeout(() => {
      if (this.token) {
        this.loginResponse.emit({
          id : '',
          token : this.token,
        })
      }
    },500)
  }

  protected onLogin() : void {
    this.registerService.login(this.loginRequest).subscribe((res : LoginResponse) => {
      this.loginResponse.emit(res)
      sessionStorage.setItem('token', res.token);
    });
  }
}
