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
import {SearchRecipePage} from "./searchRecipe";
import {RecipeService} from "../../../service/RecipeService";
import {RecipeServiceMock} from "../../../service/mocks/RecipeServiceMock.spec";



describe('SearchRecipePage', function() {
  let de: DebugElement;
  let comp: SearchRecipePage;
  let fixture: ComponentFixture<SearchRecipePage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SearchRecipePage],
      imports: [
        IonicModule.forRoot(SearchRecipePage),
        IonicStorageModule.forRoot(SearchRecipePage),
        HttpModule
      ],
      providers: [
        NavController, RealTimeService,
        {provide: Platform, useClass: PlatformMock},
        {provide: StatusBar, useClass: StatusBarMock},
        {provide: SplashScreen, useClass: SplashScreenMock},
        {provide: RecipeService, useClass: RecipeServiceMock}
      ]
    });
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchRecipePage);
    comp = fixture.componentInstance;
    de = fixture.debugElement.query(By.css('h3'));
  });

  it('should create component', () => {
    expect(comp).toBeDefined();
    expect(comp.listRecette.length).toBe(0);
    expect(comp.requestedIngredients).toBeNull();
    expect(comp.inputIngredient).toBeNull();

  });
});
