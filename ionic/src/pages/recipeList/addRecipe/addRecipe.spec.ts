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
import {equal} from "assert";
import {AddRecipePage} from "./addRecipe";
import {RecipeService} from "../../../service/RecipeService";
import {RecipeServiceMock} from "../../../service/mocks/RecipeServiceMock.spec";


describe('AddRecipePage', function() {
  let de: DebugElement;
  let comp: AddRecipePage;
  let fixture: ComponentFixture<AddRecipePage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AddRecipePage],
      imports: [
        IonicModule.forRoot(AddRecipePage),
        IonicStorageModule.forRoot(AddRecipePage),
        HttpModule
      ],
      providers: [
        NavController, RealTimeService,
        { provide: Platform, useClass: PlatformMock},
        { provide: StatusBar, useClass: StatusBarMock },
        { provide: SplashScreen, useClass: SplashScreenMock },
        { provide: RecipeService, useClass: RecipeServiceMock },

      ]
    });
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddRecipePage);
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
      name_item: "Saucisse",
      quantity: 6,
      category: "Viandes",
      taken: false
    };
    comp.listIngredients.push(new_item);
    expect(comp.listIngredients.length).toBe(1);
    expect(comp.listIngredients[0]).toEqual({name_item: "Saucisse", quantity: 6, category: "Viandes", taken: false});
  });

  it('should remove an ingredient', () => {
    expect(comp.listIngredients.length).toBe(0);
    var new_item = {
      name_item: "Saucisse",
      quantity: 6,
      category: "Viandes",
      taken: false
    };
    comp.listIngredients.push(new_item);
    expect(comp.listIngredients.length).toBe(1);
    expect(comp.listIngredients[0]).toEqual({name_item: "Saucisse", quantity: 6, category: "Viandes", taken: false});
    comp.removeIngredient(new_item);
    expect(comp.listIngredients.length).toBe(0);
  });

});
