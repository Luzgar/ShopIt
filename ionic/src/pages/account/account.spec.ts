/*import { UserService } from './../../service/UserService';
import { AccountPage } from './account';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By }           from '@angular/platform-browser';
import { DebugElement } from '@angular/core';
import { IonicModule, Platform, NavController} from 'ionic-angular/index';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { PlatformMock, StatusBarMock, SplashScreenMock } from '../../../test-config/mocks-ionic';
import {HttpModule} from "@angular/http";
import {RealTimeService} from "../../service/RealTimeService";
import {IonicStorageModule} from "@ionic/storage";

describe('AccountPage', function() {
  let de: DebugElement;
  let comp: AccountPage;
  let fixture: ComponentFixture<AccountPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AccountPage],
      imports: [
        IonicModule.forRoot(AccountPage),
        IonicStorageModule.forRoot(AccountPage),
        HttpModule
      ],
      providers: [
        NavController, RealTimeService,
        { provide: Platform, useClass: PlatformMock},
        { provide: StatusBar, useClass: StatusBarMock },
        { provide: SplashScreen, useClass: SplashScreenMock },
        UserService
      ]
    });
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AccountPage);
    comp = fixture.componentInstance;
    de = fixture.debugElement.query(By.css('h3'));
  });

  it('should create component', () => expect(comp).toBeDefined());

  it('should disconnect user', () => {
    comp.storage.set('userId', 1);
    comp.deconnect();
    comp.storage.get('userId').then((res) => {
        expect(res).toBeNull();
    });
  });

});
*/