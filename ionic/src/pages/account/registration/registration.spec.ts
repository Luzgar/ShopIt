/*import { PseudoValidator } from './../../../validators/PseudoValidator';
import { LoginValidator } from './../../../validators/LoginValidator';
import { UserService } from './../../../service/UserService';
import { Registration } from './registration';
import { RealTimeService } from './../../../service/RealTimeService';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By }           from '@angular/platform-browser';
import { DebugElement } from '@angular/core';
import { IonicModule, Platform, NavController} from 'ionic-angular/index';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { PlatformMock, StatusBarMock, SplashScreenMock } from '../../../../test-config/mocks-ionic';
import {HttpModule} from "@angular/http";
import {IonicStorageModule} from "@ionic/storage";
import {equal} from "assert";

describe('Registration', function() {
  let de: DebugElement;
  let comp: Registration;
  let fixture: ComponentFixture<Registration>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [Registration],
      imports: [
        IonicModule.forRoot(Registration),
        IonicStorageModule.forRoot(Registration),
        HttpModule
      ],
      providers: [
        NavController, RealTimeService,
        { provide: Platform, useClass: PlatformMock},
        { provide: StatusBar, useClass: StatusBarMock },
        { provide: SplashScreen, useClass: SplashScreenMock },
        UserService,
        LoginValidator,
        PseudoValidator,
      ]
    });
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(Registration);
    comp = fixture.componentInstance;
    de = fixture.debugElement.query(By.css('h3'));
  });

  it('should create component', () => expect(comp).toBeDefined());


});*/
