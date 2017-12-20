import {Component} from "@angular/core";
import { Storage } from '@ionic/storage';
import {NavController} from "ionic-angular";
import {UserService} from "../../service/UserService";
import {HomePage} from "../home/home";
import {RealTimeService} from "../../service/RealTimeService";
import {MedalService} from "../../service/MedalService";

@Component({
  selector: 'account',
  templateUrl: 'account.html',
  providers: [UserService, MedalService]
})

export class AccountPage {

  dureeListe: number;
  userId: number;
  medalsUser : Array<{title: string}>;
  exposedMedal: string;
  recurrency: boolean;

  constructor(public storage: Storage, public navCtrl: NavController, public realTimeService: RealTimeService, public userService: UserService, public medalService: MedalService) {
    this.storage.get("userId").then((data) => {
      this.userId = data;
      this.userService.getListLifeSpan(this.userId).subscribe((res) => {
        if(res == 0){
          this.dureeListe = 30;
        }
        else {
          this.dureeListe = res;
        }
      });
      this.medalsUser = [];
      this.medalService.getUserMedalUnlocked(this.userId).subscribe((res) => {
        console.log(res);
        for(var i = 0; i < res.length; ++i)
          this.medalsUser.push({title: res[i]});
      });

      this.userService.isRecurrencyActivated(this.userId).subscribe((res) => {
        this.recurrency = res;
      });
      
    });


  }



  deconnect(){
    this.storage.remove("userId");
    this.navCtrl.setRoot(HomePage);
    this.realTimeService.notifyConnection(false);
  }

  save(){
    console.log("Durée : "+ this.dureeListe);
    this.userService.setListLifeSpan(this.userId, this.dureeListe);
    this.medalService.setMedalExposed(this.userId, this.exposedMedal);
    this.userService.activateRecurrency(this.userId, this.recurrency);
    this.navCtrl.setRoot(HomePage);

  }

}
