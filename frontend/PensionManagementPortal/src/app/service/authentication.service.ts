import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { map } from 'rxjs/operators';
import { JWTToken } from '../model/jwttoken.model';


@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  

  BASE_PATH = 'http://pmp-api-lb-598347378.ap-south-1.elb.amazonaws.com';
  USER_NAME_SESSION_ATTRIBUTE_NAME = 'ADMIN';

  username: string='';
  token: string='';


  constructor(private http:HttpClient) { }

  executeJwtAuthenticationService(username:string, password:string) {
    console.log(username);
    return this.http.post<any>(`${this.BASE_PATH}/authenticate`, {
      username,
      password
    }).pipe(
      map((res: JWTToken) => {
      this.username = username;
      this.token = res.token;
      this.registerSuccessfulLoginForJwt(username);
    }));
  }

  createJWTToken(token:string) {
    return token
  }

  registerSuccessfulLoginForJwt(username:string) {
    sessionStorage.setItem(this.USER_NAME_SESSION_ATTRIBUTE_NAME, username)
  }

  registerSuccessfulLogin(username:string, password:string) {
    sessionStorage.setItem(this.USER_NAME_SESSION_ATTRIBUTE_NAME, username)
  }

  logout() {
    sessionStorage.removeItem(this.USER_NAME_SESSION_ATTRIBUTE_NAME);
    this.username = '';
    this.token = '';
  }

  isUserLoggedIn() {
    let user = sessionStorage.getItem(this.USER_NAME_SESSION_ATTRIBUTE_NAME)
    if (user === null) return false
    return true
  }

  getLoggedInUserName() {
    let user = sessionStorage.getItem(this.USER_NAME_SESSION_ATTRIBUTE_NAME)
    if (user === null) return ''
    return user
  }
}
