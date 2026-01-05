import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {LoginRequest} from "../entities/login-request";
import {Observable} from "rxjs";
import {LoginResponse} from "../entities/login-response";
import {environment} from "../../environments/environment.development";

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  private readonly baseUrl: string = environment.baseUrl+'/register';

  constructor(private readonly httpClient: HttpClient) { }

  public login(loginRequest: LoginRequest): Observable<LoginResponse> {
    return this.httpClient.post<LoginResponse>(this.baseUrl+'/login', loginRequest)
  }

}
