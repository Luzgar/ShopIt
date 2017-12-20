import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By }           from '@angular/platform-browser';
import { DebugElement } from '@angular/core';
import {IonicModule, Platform, NavController, NavParams} from 'ionic-angular/index';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { PlatformMock, StatusBarMock, SplashScreenMock } from '../../../../test-config/mocks-ionic';
import {HttpModule} from "@angular/http";
import {RealTimeService} from "../../../service/RealTimeService";
import {IonicStorageModule} from "@ionic/storage";
import {DescriptionModalPage} from "./descriptionModal";
import {equal} from "assert";
import {RecipeService} from "../../../service/RecipeService";
import {RecipeServiceMock} from "../../../service/mocks/RecipeServiceMock.spec";

class MockNavParams{
  data = {
    key: 1
  };

  get(param){
    return this.data[param];
  }
}


describe('DescriptionModalPage', function() {
  let de: DebugElement;
  let comp: DescriptionModalPage;
  let fixture: ComponentFixture<DescriptionModalPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DescriptionModalPage],
      imports: [
        IonicModule.forRoot(DescriptionModalPage),
        IonicStorageModule.forRoot(DescriptionModalPage),
        HttpModule
      ],
      providers: [
        NavController, RealTimeService,
        { provide: Platform, useClass: PlatformMock},
        { provide: StatusBar, useClass: StatusBarMock },
        { provide: SplashScreen, useClass: SplashScreenMock },
        { provide: NavParams, useClass: MockNavParams},
        { provide: RecipeService, useClass: RecipeServiceMock}

      ]
    });
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DescriptionModalPage);
    comp = fixture.componentInstance;
    de = fixture.debugElement.query(By.css('h3'));
  });

  it('should create component', () => {
    expect(comp).toBeDefined();
    expect(comp.listIngredients.length).toBe(0);
  });


  it('should add an ingredient', () => {
    expect(comp.listIngredients.length).toBe(0);
    var new_item = {
      item: "Saucisse",
      quantity: 6,
      category: "Viandes",
      taken: false
    };
    comp.listIngredients.push(new_item);
    expect(comp.listIngredients.length).toBe(1);
    expect(comp.listIngredients[0] === {item: "Saucisse", quantity: 6, category: "Viandes", taken: false});

  });

  it('should delete an ingredient', () => {
    expect(comp.listIngredients.length).toBe(0);
    var new_item = {
      item: "Saucisse",
      quantity: 6,
      category: "Viandes",
      taken: false
    };
    comp.listIngredients.push(new_item);
    expect(comp.listIngredients.length).toBe(1);
    expect(comp.listIngredients[0] === {item: "Saucisse",quantity: 6, category: "Viandes", taken: false});
    comp.deleteIngredient(new_item);
    expect(comp.listIngredients.length).toBe(0);

  });



});
