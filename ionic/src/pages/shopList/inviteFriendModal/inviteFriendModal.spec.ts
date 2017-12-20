import { ListServiceMock } from './../../../service/mocks/ListServiceMock.spec';
import { ListService } from './../../../service/ListService';
import { NavParams } from 'ionic-angular';
import { InviteFriend } from './inviteFriendModal';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By }           from '@angular/platform-browser';
import { DebugElement } from '@angular/core';
import { IonicModule, Platform, NavController} from 'ionic-angular/index';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { PlatformMock, StatusBarMock, SplashScreenMock } from '../../../../test-config/mocks-ionic';
import {HttpModule} from "@angular/http";
import {RealTimeService} from "../../../service/RealTimeService";
import {IonicStorageModule} from "@ionic/storage";

class MockNavParams{
  data = {
    collabInfos: "",
    listId: 1,
    ownerPseudo: "coco"
  };

  get(param){
    return this.data[param];
  }
}

describe('InviteFriend', function() {
  let de: DebugElement;
  let comp: InviteFriend;
  let fixture: ComponentFixture<InviteFriend>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [InviteFriend],
      imports: [
        IonicModule.forRoot(InviteFriend),
        IonicStorageModule.forRoot(InviteFriend),
        HttpModule
      ],
      providers: [
        NavController, RealTimeService,
        { provide: Platform, useClass: PlatformMock},
        { provide: StatusBar, useClass: StatusBarMock },
        { provide: SplashScreen, useClass: SplashScreenMock },
        { provide: NavParams, useClass: MockNavParams },
        { provide: ListService, useClass: ListServiceMock },
      ]
    });
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InviteFriend);
    comp = fixture.componentInstance;
    de = fixture.debugElement.query(By.css('h3'));
  });

  it('should create component', () => expect(comp).toBeDefined());


});
