import { Component, ViewChild } from '@angular/core';
import {Nav, Platform} from 'ionic-angular';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { Push, PushToken} from '@ionic/cloud-angular';

import { HomePage } from '../pages/home/home';
import {RecipeListPage} from "../pages/recipeList/recipeList";
import {RealTimeService} from "../service/RealTimeService";
import {LocalNotifications} from "@ionic-native/local-notifications";
import {ShopListPage} from "../pages/shopList/shopList";
import {Registration} from "../pages/account/registration/registration";
import {Authentification} from "../pages/account/authentification/authentification";
import {AccountPage} from "../pages/account/account";
import { Storage } from '@ionic/storage';
import {PhotoReceipt} from "../pages/photoReceipt/photoreceipt";
import { Network } from "ionic-native/dist/es5";

@Component({
  templateUrl: 'app.html'
})

export class MyApp {
  @ViewChild(Nav) nav: Nav;

  rootPage: any = HomePage;

  pages: Array<{title: string, icon:string, component: any}>;

  isConnected: boolean;

  constructor(public storage: Storage, public realTimeService : RealTimeService, public platform: Platform, public push: Push, public statusBar: StatusBar, public splashScreen: SplashScreen, private localNotifications : LocalNotifications) {
    this.initializeApp();
    this.isConnected = false;
    this.storage.get("userId").then((data)=>{
      this.pages = [
        { title: 'Mes Listes', icon: 'list', component: HomePage },
        { title: 'Recettes', icon: 'flask',component: RecipeListPage },
        { title: 'Photo', icon: 'camera',component: PhotoReceipt }
      ];
      if(data == null){
        this.pages.push({ title: 'S\'inscrire', icon: 'create',component: Registration });
        this.pages.push({ title: 'Se connecter', icon: 'log-in',component: Authentification });
      }
      else {
        this.pages.push({ title: 'Compte', icon: 'contact',component: AccountPage });
      }
    });

    if (platform.is('cordova')) {
    this.push.register().then((t: PushToken) => {
      return this.push.saveToken(t);
    }).then((t: PushToken) => {
      console.log(t.token);
    });
    this.push.rx.notification()
      .subscribe((msg) => {
      console.log(msg);

        switch (msg.raw.additionalData.type){
          case "list_invitation":
            if(msg.raw.additionalData.foreground) {
              this.localNotifications.schedule({
                id: Date.now(),
                title: msg.title,
                text: msg.text
              });
              this.localNotifications.on('click', ()=> {
                this.nav.push(ShopListPage,{listId: msg.raw.additionalData.list_id, nameShopList: msg.raw.additionalData.list_title});
              });
              this.realTimeService.notifyTitleChange("Notifie");
            }
            else {
              this.nav.push(ShopListPage,{listId: msg.raw.additionalData.list_id, nameShopList: msg.raw.additionalData.list_title});
            }

            break;
          case "list_modification":
            this.realTimeService.notifyListChange("Notifie");
            break;
          case "list_repartition":
            this.realTimeService.notifyListChange("Notifie");
            break;
          case "list_expulsion":
            if(msg.raw.additionalData.foreground) {
              this.localNotifications.schedule({
                id: Date.now(),
                title: msg.title,
                text: msg.text
              });
              this.realTimeService.notifyTitleChange("Notifie");
              this.realTimeService.notifyListChange("Notifie");
            }

            break;
        }
      });
    }

    this.realTimeService.getNotifyConnection().subscribe(isConnected => {
      console.log("ISCONNECTED");
      this.pages = [
        { title: 'Mes Listes', icon: 'list',component: HomePage },
        { title: 'Recettes', icon: 'flask',component: RecipeListPage },
        { title: 'Photo', icon: 'camera',component: PhotoReceipt }
      ];
      if(!isConnected){
        this.pages.push({ title: 'S\'inscrire', icon: 'create',component: Registration });
        this.pages.push({ title: 'Se connecter', icon: 'log-in',component: Authentification });
      }
      else {
        this.pages.push({ title: 'Compte', icon: 'contact',component: AccountPage });
      }
    });
    // used for an example of ngFor and navigation
  }

  initializeApp() {
    this.platform.ready().then(() => {
      // Okay, so the platform is ready and our plugins are available.
      // Here you can do any higher level native things you might need.
      this.statusBar.styleDefault();
      this.splashScreen.hide();
      let disconnectSub = Network.onDisconnect().subscribe(() => {
        console.log("offline");
        this.realTimeService.notifyInternetConnection(false);
      });

      let connectSub = Network.onConnect().subscribe(()=> {
        console.log("online");
        this.realTimeService.notifyInternetConnection(true);
      });
    });
  }

  openPage(page) {
    // Reset the content nav to have just this page
    // we wouldn't want the back button to show in this scenario
    if(page.title == 'S\'inscrire' || page.title == 'Se connecter')
      this.nav.push(page.component);
    else
      this.nav.setRoot(page.component);
  }
}
