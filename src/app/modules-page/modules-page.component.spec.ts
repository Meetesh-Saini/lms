import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModulesPageComponent } from './modules-page.component';

describe('ModulesPageComponent', () => {
  let component: ModulesPageComponent;
  let fixture: ComponentFixture<ModulesPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModulesPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModulesPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
