/*import { ArchivedList } from './archiveList';
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
import {equal} from "assert";

describe('ArchivedList', function() {
  let de: DebugElement;
  let comp: ArchivedList;
  let fixture: ComponentFixture<ArchivedList>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ArchivedList],
      imports: [
        IonicModule.forRoot(ArchivedList),
        IonicStorageModule.forRoot(ArchivedList),
        HttpModule
      ],
      providers: [
        NavController,
        { provide: Platform, useClass: PlatformMock},
        { provide: StatusBar, useClass: StatusBarMock },
        { provide: SplashScreen, useClass: SplashScreenMock },
        RealTimeService
      ]
    });
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ArchivedList);
    comp = fixture.componentInstance;
    comp.listes = [];
    de = fixture.debugElement.query(By.css('h3'));
  });

  it('should create component', () => expect(comp).toBeDefined());

  it('should delete an archived shop list', () => {
    
    comp.listes.push({title : "Coco", listId: 1});
    expect(comp.listes.length).toBe(1);
    comp.deleteArchivedList({title : "Coco", listId: 1}).then(()=> {
        expect(comp.listes.length).toBe(0);
    });
  });

});*/
