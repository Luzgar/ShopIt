import { Network } from '@ionic-native/network';
import { BrowserModule } from '@angular/platform-browser';
import { ErrorHandler, NgModule } from '@angular/core';
import { IonicApp, IonicErrorHandler, IonicModule } from 'ionic-angular';
import { IonicStorageModule } from '@ionic/storage';
import { MyApp } from './app.component';
import { HomePage } from '../pages/home/home';


import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import {HttpModule} from "@angular/http";
import {RecipeListPage} from "../pages/recipeList/recipeList";
import {KeysPipe, ShopListPage} from "../pages/shopList/shopList";
import {SelectListToCompletePage} from "../pages/selectListToComplete/selectListToComplete";
import {InviteFriend} from "../pages/shopList/inviteFriendModal/inviteFriendModal";
import { CloudSettings, CloudModule } from '@ionic/cloud-angular';
import {RealTimeService} from "../service/RealTimeService";
import {LocalNotifications} from "@ionic-native/local-notifications";
import {Registration} from "../pages/account/registration/registration";
import {Authentification} from "../pages/account/authentification/authentification";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {LoginValidator} from "../validators/LoginValidator";
import {UserService} from "../service/UserService";
import {PseudoValidator} from "../validators/PseudoValidator";
import {AccountPage} from "../pages/account/account";
import {PhotoReceipt} from "../pages/photoReceipt/photoreceipt";
import {DescriptionModalPage} from "../pages/recipeList/descriptionModal/descriptionModal";
import {AddRecipePage} from "../pages/recipeList/addRecipe/addRecipe";
import {CurrentList} from "../pages/home/currentList/currentList";
import {ArchivedList} from "../pages/home/archiveList/archiveList";
import {SearchRecipePage} from "../pages/recipeList/searchRecipe/searchRecipe";


const cloudSettings: CloudSettings = {
  'core': {
    'app_id': 'd48e1e91'
  },
  'push': {
    'sender_id': '342464945757',
    'pluginConfig': {
      'ios': {
        'badge': true,
        'sound': true
      },
      'android': {
        'iconColor': '#343434'
      }
    }
  }
};

@NgModule({
  declarations: [
    MyApp,
    HomePage,
    ShopListPage,
    KeysPipe,
    RecipeListPage,
    SelectListToCompletePage,
    InviteFriend,
    Registration,
    Authentification,
    DescriptionModalPage,
    AccountPage,
    PhotoReceipt,
    AddRecipePage,
    CurrentList,
    ArchivedList,
    SearchRecipePage
  ],
  imports: [
    BrowserModule,
    IonicModule.forRoot(MyApp, {
      tabsPlacement: 'bottom',
      tabsHideOnSubPages:"true",
      scrollAssist: false,
      platforms: {
        android: {
          tabsPlacement: 'top'
        },
        ios: {
          tabsPlacement: 'top'
        },
        windows:
          {
            tabsPlacement: 'top'
          }
      }
    }),
    HttpModule,
    IonicStorageModule.forRoot(),
    CloudModule.forRoot(cloudSettings),
    FormsModule,
    ReactiveFormsModule
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    HomePage,
    ShopListPage,
    RecipeListPage,
    SelectListToCompletePage,
    InviteFriend,
    Registration,
    Authentification,
    DescriptionModalPage,
    AccountPage,
    PhotoReceipt,
    AddRecipePage,
    CurrentList,
    ArchivedList,
    SearchRecipePage
  ],
  providers: [
    StatusBar,
    SplashScreen,
    RealTimeService,
    LocalNotifications,
    LoginValidator,
    PseudoValidator,
    UserService,
    Network,
    StatusBar,
    {provide: ErrorHandler, useClass: IonicErrorHandler}
  ]
})

export class AppModule {}
