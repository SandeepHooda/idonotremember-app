import { Component, OnInit } from '@angular/core';

import {DeviceService} from '../home/services/deviceservice'

import {DeleteAll_VO} from '../home/domain/DeleteAll_VO';
import {Settings} from '../home/domain/settings';
import {Phone} from '../home/domain/phone';
@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  deletefoscamInboxDaily : boolean;
  useGoogleVisionanalysis : boolean;
  public phone1 :String;
  public phone2 :String;
  public phone3 :String;
  public phone4 :String;
  public phone5 :String;
  constructor(private deviceService: DeviceService) { 
      
  }

  ngOnInit() {
    
    this.getEmailAutoDeleteStatus();
    this.getGoogleVisionSettings();
    this.getPhoneNumbers();
   }

   public getPhoneNumbers ():void{
    this.deviceService.getPhoneNumbers().subscribe(
      (phoneResponseVO: Phone) => {
        this.phone1 = phoneResponseVO.phoneNumbers[0]
        this.phone2 = phoneResponseVO.phoneNumbers[1]
        this.phone3 = phoneResponseVO.phoneNumbers[2]
        this.phone4 = phoneResponseVO.phoneNumbers[3]
        this.phone5 = phoneResponseVO.phoneNumbers[4]
        
        }, error => {         
      }
    );
   }
   public updatePhones ():void{
     
      let phoneVo : Phone = new Phone();
      phoneVo._id = "0";
      phoneVo.phoneNumbers = [];
      if (this.phone1){
        phoneVo.phoneNumbers.push(this.phone1)
      }
      if (this.phone2){
        phoneVo.phoneNumbers.push(this.phone2)
      }
      if (this.phone3){
        phoneVo.phoneNumbers.push(this.phone3)
      }
      if (this.phone4){
        phoneVo.phoneNumbers.push(this.phone4)
      }
      if (this.phone5){
        phoneVo.phoneNumbers.push(this.phone5)
      }
    this.deviceService.updatePhones(phoneVo).subscribe(
      (phoneResponseVO: Phone) => {
        this.phone1 = phoneResponseVO.phoneNumbers[0]
        this.phone2 = phoneResponseVO.phoneNumbers[1]
        this.phone3 = phoneResponseVO.phoneNumbers[2]
        this.phone4 = phoneResponseVO.phoneNumbers[3]
        this.phone5 = phoneResponseVO.phoneNumbers[4]
        
        }, error => {         
      }
    );
   }
   public getGoogleVisionSettings():void{
    this.deviceService.getGoogleVisionSettings().subscribe(
      (status: Settings) => {
        this.useGoogleVisionanalysis = status.value;
        }, error => {         
      }
    );
  }
  public getEmailAutoDeleteStatus():void{
    this.deviceService.getEmailAutoDeleteStatus().subscribe(
      (status: DeleteAll_VO) => {
        this.deletefoscamInboxDaily = status.deleteAll;
        }, error => {         
      }
    );
  }

  public toggleEmailDeletePrcocess():void{
    this.deviceService.toggleEmailDeletePrcocess().subscribe(
      (status: DeleteAll_VO) => {
        this.deletefoscamInboxDaily = status.deleteAll;
        }, error => {         
      }
    );
  }

  public toggleGooglevisionanalysisSettings():void{
    this.deviceService.toggleGooglevisionanalysisSettings().subscribe(
      (status: Settings) => {
        this.useGoogleVisionanalysis = status.value;
        }, error => {         
      }
    );
  }
}
