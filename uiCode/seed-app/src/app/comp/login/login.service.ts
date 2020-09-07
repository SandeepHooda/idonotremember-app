import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {LoginVO}  from './LoginVO';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class LoginService {
  constructor(private http: HttpClient) { }

  validateLogin(regID: String) :  Observable<LoginVO> {
    return this.http.get<LoginVO>('/ws/login/validate/'+regID+'/timeZone/'+Intl.DateTimeFormat().resolvedOptions().timeZone.replace("/", "@"));
  }
}