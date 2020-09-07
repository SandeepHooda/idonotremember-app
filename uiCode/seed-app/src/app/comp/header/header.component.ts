import { Component, OnInit } from '@angular/core';
import {Global} from '../global/global';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(private global:Global) { }

   openNav():void {
     if (!this.global.navigationDisabled){
        document.getElementById("mySidenav").style.width = "250px";
     }else {
      document.getElementById("mySidenav").style.width = "0px";
     }
    
  }
  
   closeNav():void {
    document.getElementById("mySidenav").style.width = "0px";
  }

  ngOnInit() {
  }

}
