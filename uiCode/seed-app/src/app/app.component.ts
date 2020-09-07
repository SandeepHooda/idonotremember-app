import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import {LoginService} from './comp/login/login.service';
import {LoginVO} from './comp/login/loginVO';
import {Global} from './comp/global/global';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  
})
export class AppComponent implements OnInit{
  regID:String;
  title = 'Home Security';

  constructor(private _router: Router,private httpClient: HttpClient, 
    private loginService : LoginService, private global:Global){}

  ngOnInit() {
    let  local : Boolean= false;
    
    if (window.location.href.indexOf("localhost") >= 0){
      local = true;
      window.localStorage.setItem('regID', 'a6f44435-61a6-4ab4-adab-721ccf7accfc');
    }
    
    this.regID = this.getCookie('regID');
    console.log("AppComponent init method reg id from cookie "+this.regID)
    if (this.regID == null || this.regID  === '' ){
      this.regID = window.localStorage.getItem('regID');
    }
    
    this.global.navigationDisabled = true;
    //this.validateRegID(this.regID);
    
    console.log("AppComponent init method reg id "+this.regID+" url :  "+window.location.href)
  }

  validateRegID (regID: String): void {
    
     
      if(regID){
        this.loginService.validateLogin(regID).subscribe(
          (loginVO: LoginVO) => {
            if (loginVO != null && loginVO.emailID){
              window.localStorage.setItem('regID', ''+this.regID);
            }else {
              this.showLoginPage();
            }
          }, error => {
            console.log(error)
            this.showLoginPage();
          }
        );
      }else {
        this.showLoginPage();
      }

  }
  showLoginPage():void{
    this.global.navigationDisabled = true;
    this._router.navigate(['login']);
  }
  getCookie(cname:String):String {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}
}
