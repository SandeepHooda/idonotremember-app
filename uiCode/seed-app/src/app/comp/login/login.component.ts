import { Component, OnInit } from '@angular/core';
import {LoginService} from './login.service';
import {LoginVO} from './loginVO';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private loginService : LoginService) { }

  ngOnInit() {
    
    }

  signIN () :void{
    if(window.localStorage.getItem('regID')){
      localStorage.removeItem('regID');
      localStorage.removeItem('name');
    }
    
    window.open("/Oauth", "_self");
  }

  
}
