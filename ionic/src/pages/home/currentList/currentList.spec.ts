/*import { RealTimeService } from './../../../service/RealTimeService';
import { ListService } from './../../../service/ListService';
import { CurrentList } from './currentList';
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

describe('CurrentList', function() {
  let de: DebugElement;
  let comp: CurrentList;
  let fixture: ComponentFixture<CurrentList>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CurrentList],
      imports: [
        IonicModule.forRoot(CurrentList),
        IonicStorageModule.forRoot(CurrentList),
        HttpModule
      ],
      providers: [
        NavController,
        { provide: Platform, useClass: PlatformMock},
        { provide: StatusBar, useClass: StatusBarMock },
        { provide: SplashScreen, useClass: SplashScreenMock },
        RealTimeService,
        ListService
      ]
    });
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CurrentList);
    comp = fixture.componentInstance;
    de = fixture.debugElement.query(By.css('h3'));
  });

  it('should create component', () => expect(comp).toBeDefined());

  it('should add a shop list', () => {
    var id = Date.now()*-1;
    comp.addList("Coco", id).then(() => {
      expect(comp.listes.length).toBe(1);
      expect(equal(comp.listes[0],  {title: "Coco", id: id}));
    });

  });

  it("should delete a shop list", () => {
    var id = Date.now()*-1;
    comp.addList("Coco", id).then(() => {
      expect(comp.listes.length).toBe(1);
      expect(equal(comp.listes[0],  {title: "Coco", id: id}));
      comp.deleteCurrentList({title: "Coco", id: id}).then(()=> {
        expect(comp.listes.length).toBe(0);
      });

    });

  });

  it('should archive a Shop list', () => {
    var id = Date.now()*-1;
    comp.addList("Coco", id).then(() => {
      expect(comp.listes.length).toBe(1);
      expect(equal(comp.listes[0],  {title: "Coco", id: id}));
      
      comp.archiveShopList({title: "Coco", id: id}).then(() => {
        expect(comp.listes.length).toBe(0);
      });

    });
  });
});*/
