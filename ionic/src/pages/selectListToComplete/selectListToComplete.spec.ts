import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By }           from '@angular/platform-browser';
import { DebugElement } from '@angular/core';
import {IonicModule, Platform, NavController, NavParams} from 'ionic-angular/index';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { PlatformMock, StatusBarMock, SplashScreenMock } from '../../../test-config/mocks-ionic';
import {HttpModule} from "@angular/http";
import {RealTimeService} from "../../service/RealTimeService";
import {IonicStorageModule} from "@ionic/storage";
import {SelectListToCompletePage} from "./selectListToComplete";
import {ListService} from "../../service/ListService";
import {ListServiceMock} from "../../service/mocks/ListServiceMock.spec";


class MockNavParams{
  data = {
    listIngredients: [{item: "Saucisse",quantity: 6, category: "Viandes", taken: false}],
    name: "Eclair à la saucisse"
  };

  get(param){
    return this.data[param];

  }
}


describe('SelectListToCompletePage', function() {
  let de: DebugElement;
  let comp: SelectListToCompletePage;
  let fixture: ComponentFixture<SelectListToCompletePage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SelectListToCompletePage],
      imports: [
        IonicModule.forRoot(SelectListToCompletePage),
        IonicStorageModule.forRoot(SelectListToCompletePage),
        HttpModule
      ],
      providers: [
        NavController, RealTimeService,
        { provide: Platform, useClass: PlatformMock},
        { provide: StatusBar, useClass: StatusBarMock },
        { provide: SplashScreen, useClass: SplashScreenMock },
        { provide: NavParams, useClass: MockNavParams},
        { provide: ListService, useClass: ListServiceMock}

      ]
    });
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectListToCompletePage);
    comp = fixture.componentInstance;
    de = fixture.debugElement.query(By.css('h3'));
  });

  it('should create component', () => {
    expect(comp).toBeDefined();
    expect(comp.itemList.length).toBe(0);
    expect(comp.listIngredients.length).toBe(1);
    expect(comp.listIngredients[0]).toEqual({item: "Saucisse",quantity: 6, category: "Viandes", taken: false});
    expect(comp.nameRecipe).toEqual("Eclair à la saucisse");

  })



});
