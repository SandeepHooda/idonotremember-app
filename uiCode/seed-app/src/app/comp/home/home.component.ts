import { Component, OnInit } from '@angular/core';
import { Device } from './domain/device';

import {DeviceService} from './services/deviceservice'
import { IpAddress } from './domain/ipAddress';
import {DeleteAll_VO} from './domain/DeleteAll_VO';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

   devices : Device[];
   cols: any[];
   externalIPString :String;
   ipAddress:String;
   systemInGoodhealth :boolean = true;
   constructor(private deviceService: DeviceService) { 
      
  }

  ngOnInit() {
    this.getDeviceStatus();
    this.getExternalIP();
   
   }

   refresh():void{
    this.getDeviceStatus();
    this.getExternalIP();
  }
  private  getExternalIP():void{
    this.deviceService.getExternalIP().subscribe(
      (ipAddress: IpAddress) => {
        let date :Date = new Date(ipAddress.time);
        this.ipAddress = ipAddress.myIP;
        this.externalIPString =this.ipAddress +" "+ date.toString() ;
        this.externalIPString =this.externalIPString.substr(0,this.externalIPString.indexOf('GMT'))
        }, error => {         
      }
    );
  }
  private  getDeviceStatus():void{

    this.deviceService.getDevices().subscribe(
      (devices: Array<Device>) => {
        this.getDisplayDate(devices);
        this.devices = devices
        }, error => {         
      }
    );
  }
  
  
  private  updateDevice(device:Device):void{
    if (device.alertType_emailOnly){
      device.turnOnHealthCheck = true;
      device.disabledHelthCheckbox = true;
    }else {
      device.disabledHelthCheckbox = false;
    }
    let allDevices =  Array<Device>();
    allDevices.push(device);
    this.deviceService.updateDevice(allDevices).subscribe(
      (devices: Array<Device>) => {
        this.getDisplayDate(devices);
        this.devices = [];
        this.devices = devices
        }, error => {         
      }
    );
  }

    enableAll():void{
    for (let i=0; i<this.devices.length;i++){
      this.devices[i].turnOnHealthCheck = true;
    }
    this.deviceService.updateDevice(this.devices).subscribe(
      (devices: Array<Device>) => {
        this.getDisplayDate(devices);
        this.devices = [];
        this.devices = devices
        }, error => {         
      }
    );
  }

    disableAll():void{
    for (let i=0; i<this.devices.length;i++){
      this.devices[i].turnOnHealthCheck = false;
    }
    this.deviceService.updateDevice(this.devices).subscribe(
      (devices: Array<Device>) => {
        this.getDisplayDate(devices);
        this.devices = [];
        this.devices = devices
        }, error => {         
      }
    );
  }
  private getDisplayDate(devices: Array<Device>):void{
    this.systemInGoodhealth = true;
    for (let i=0;i<devices.length;i++){
      if (devices[i].deviceType != 'doorSensor'){
        continue;
      }
      if ( new Date().getTime() - devices[i].healthCheckTime > devices[i].heatBeatTimeout ){// 5 minutes
        devices[i].unplugged = true;
        if (devices[i].turnOnHealthCheck){
          this.systemInGoodhealth = false;//Un plugged and device is being monitored
        }
      }
      if (devices[i].turnOnHealthCheck && devices[i].alarmTriggered){
        this.systemInGoodhealth = false;//Alarmed and device is being monitored
      }
      let date :Date = new Date(devices[i].healthCheckTime);
      devices[i].healthCheckTimeDisplay = date.toString();
      devices[i].healthCheckTimeDisplay = devices[i].healthCheckTimeDisplay.substr(0,devices[i].healthCheckTimeDisplay.indexOf('GMT'))
      date = new Date(devices[i].lastAlarmTime);
      devices[i].lastAlarmTimeDisplay = date.toString();
      devices[i].lastAlarmTimeDisplay = devices[i].lastAlarmTimeDisplay.substr(0,devices[i].lastAlarmTimeDisplay.indexOf('GMT'))
      

    }
    
  }
  private  moakDrill(_id:string):void{
    window.open("https://sanhoo-home-security.appspot.com/IamAlive?id="+_id+"&alarmTriggered=y");
  }
   foscamOpen():void{
    window.open("http://"+this.ipAddress+":7080/");
  }
  d3dOpen():void{
    window.open("http://"+this.ipAddress+":5000/");
  }
  piCamOpen():void{
    window.open("http://"+this.ipAddress+":8000/");
  }

  piOpen():void{
    window.open("http://"+this.ipAddress+":8080/");
  }
   
  
}

