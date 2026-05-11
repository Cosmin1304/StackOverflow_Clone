import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AskQuestionComponent } from './ask-question.component';

describe('AskQuestion', () => {
  let component: AskQuestionComponent;
  let fixture: ComponentFixture<AskQuestionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AskQuestionComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(AskQuestionComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
