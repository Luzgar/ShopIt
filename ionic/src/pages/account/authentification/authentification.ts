import {Component} from "@angular/core";
import {NavController, NavParams} from "ionic-angular";
import {UserService} from "../../../service/UserService";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HomePage} from "../../home/home";
import { Push, PushToken} from '@ionic/cloud-angular';
import {Nav, Platform} from 'ionic-angular';
import { Storage } from '@ionic/storage';
import {getPages} from "@ionic/cli-plugin-ionic-angular/dist/utils/generate";
import {MyApp} from "../../../app/app.component";
import {RealTimeService} from "../../../service/RealTimeService";

@Component({
  selector: 'authentification',
  templateUrl: 'authentification.html',
  providers: [UserService]
})

export class Authentification {

  submitAttempt: boolean;
  authForm : FormGroup;
  error: boolean;

  constructor(public storage: Storage, public platform: Platform, public push : Push, public navCtrl: NavController, private userService : UserService, public formBuilder: FormBuilder, public realTimeService : RealTimeService) {
    this.error = false;
    this.authForm = formBuilder.group({

      login: [''],
      password: ['']
    });
  }

  authenticate(){
    this.submitAttempt = true;

    console.log(this.authForm.value);
    this.userService.authenticate(this.authForm.value.login, this.authForm.value.password).subscribe(res => {
      if (this.platform.is('cordova')) {
        this.push.register().then((t: PushToken) => {
          return this.push.saveToken(t);
        }).then((t: PushToken) => {
          this.userService.sendDeviceToken(res, t.token).subscribe(() => {
            //this.navCtrl.setRoot(HomePage);
          });
        });
      }
      this.storage.set("userId", res);
      this.realTimeService.notifyConnection(true);
      this.navCtrl.setRoot(HomePage);
    }, err => {
      this.error = true;
    });

  }

}
