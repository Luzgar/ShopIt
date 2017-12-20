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
import {RecipeListPage} from "./recipeList";
import {RecipeService} from "../../service/RecipeService";
import {RecipeServiceMock} from "../../service/mocks/RecipeServiceMock.spec";

describe('RecipeListPage', function() {
  let de: DebugElement;
  let comp: RecipeListPage;
  let fixture: ComponentFixture<RecipeListPage>;



  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [RecipeListPage],
      imports: [
        IonicModule.forRoot(RecipeListPage),
        IonicStorageModule.forRoot(RecipeListPage),
        HttpModule
      ],
      providers: [
        NavController, RealTimeService,
        { provide: Platform, useClass: PlatformMock},
        { provide: StatusBar, useClass: StatusBarMock },
        { provide: SplashScreen, useClass: SplashScreenMock },
        { provide: RecipeService, useClass: RecipeServiceMock  }

      ]
    });
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RecipeListPage);
    comp = fixture.componentInstance;
    de = fixture.debugElement.query(By.css('h3'));
  });

  it('should create component', () => expect(comp).toBeDefined());


});
