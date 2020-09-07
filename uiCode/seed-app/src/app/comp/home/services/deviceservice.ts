import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Device } from '../domain/device';
import { IpAddress } from '../domain/ipAddress';

import { HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import {DeleteAll_VO} from '../domain/DeleteAll_VO';
import {Settings} from '../domain/settings';
import {Phone} from '../domain/phone';

@Injectable()
export class DeviceService {
    private httpOptions = {
        headers: new HttpHeaders({
          'Content-Type':  'application/json',
          'Authorization': ''+window.localStorage.getItem('regID')
        })
      };

    constructor(private http: HttpClient) {}

    getDevices(): Observable<Array<Device>>{
      let url = '/Devices';
      if (window.location.href.indexOf('appspot.com') < 0){
        url = 'assets/data/devices.json';
      }
        return this.http.get<Array<Device>>(url,this.httpOptions);
    }
    getPhoneNumbers(): Observable<Phone>{
      return this.http.get<Phone>('/PhoneNumbers',this.httpOptions);
    }

    getExternalIP(): Observable<IpAddress>{
      return this.http.get<IpAddress>('/MyExternalIP'
          ,this.httpOptions);
  }

    updateDevice(device:Array<Device>): Observable<Array<Device>> {
       return this.http.post<Array<Device>>('/Devices', device,this.httpOptions);
    }
    updatePhones(phones:Phone): Observable<Phone> {
      return this.http.post<Phone>('/PhoneNumbers', phones,this.httpOptions);
   }
    toggleEmailDeletePrcocess(): Observable<DeleteAll_VO> {
      return this.http.post<DeleteAll_VO>('/DeleteAllEmails', null,this.httpOptions);
   }
   toggleGooglevisionanalysisSettings(): Observable<Settings> {
    return this.http.post<Settings>('/Settings', null,this.httpOptions);
 }
   getEmailAutoDeleteStatus(): Observable<DeleteAll_VO> {
    return this.http.get<DeleteAll_VO>('/DeleteAllEmails?getStatus=true', this.httpOptions);
   }

    getGoogleVisionSettings(): Observable<Settings> {
      return this.http.get<Settings>('/Settings', this.httpOptions);
 }
   
}
