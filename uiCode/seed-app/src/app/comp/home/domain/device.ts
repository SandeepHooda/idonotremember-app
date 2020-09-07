export interface Device {
    _id : string;
    displayOrder : number;
    name: string;
    healthCheckTime :number;
    healthCheckTimeDisplay :string;
    lastAlarmTime : number;
    heatBeatTimeout : number;
    lastAlarmTimeDisplay :string;
    turnOnHealthCheck:boolean;
    alarmTriggered:boolean;
    unplugged:boolean;
    deviceType : String;
    alertType_emailOnly: boolean;
    disabledHelthCheckbox:boolean;
   
}
