/// <reference types="cypress" />

import { QuestionDetailComponent } from './question-detail.component';
import { QuestionService } from '../services/question.service';
import { ActivatedRoute } from '@angular/router';

describe('QuestionDetailComponent - Testare de componenta', () => {

  const mockQuestion = {
    id: 1,
    title: 'Cum centrez un div?',
    text: 'Am încercat totul, dar nu reușesc.',
    author: { id: 1, username: 'cosmin_dev' },
    createdAt: new Date('2026-05-14T10:00:00'),
    voteCount: 5,
    tags: [{ name: 'css' }]
  };

  const mockAnswers = [
    {
      id: 10,
      text: 'Folosește flexbox!',
      author: { id: 2, username: 'alt_user' },
      createdAt: new Date('2026-05-14T10:05:00'),
      voteCount: 10
    }
  ];

  //configurare generala refolosita pentru mount
  const setupComponent = (userRole: string = 'USER', userId: number = 2) => {
    const activatedRouteMock = {
      snapshot: { paramMap: { get: () => '1' } }
    };

    const questionServiceMock = {
      getQuestionById: cy.stub().returns(mockQuestion),
      getAnswersForQuestion: cy.stub().returns(mockAnswers)
    };

    cy.mount(QuestionDetailComponent, {
      providers: [
        { provide: ActivatedRoute, useValue: activatedRouteMock },
        { provide: QuestionService, useValue: questionServiceMock }
      ],
      //setarea datelor, inainte de randarea initiala
      componentProperties: {
        currentUser: { id: userId, username: 'test_user', role: userRole }
      }
    });
  };

  it('ar trebui să randeze întrebarea și detaliile ei', () => {
    setupComponent();
    cy.get('.question-header h2').should('contain.text', 'Cum centrez un div?');
    cy.get('.question-body p').should('contain.text', 'Am încercat totul');
    cy.get('.meta-info').should('contain.text', 'cosmin_dev');
    cy.get('.vote-count').first().should('have.text', '5');
  });

  it('ar trebui să randeze lista de răspunsuri', () => {
    setupComponent();
    cy.get('.answers-section h3').should('contain.text', '1 Răspunsuri');
    cy.get('.answer-card').should('have.length', 1);
    cy.get('.answer-text').should('contain.text', 'Folosește flexbox!');
    cy.get('.answer-meta-block').should('contain.text', 'alt_user');
  });

  it('NU ar trebui să afișeze butoanele de Edit/Delete dacă NU ești autorul sau moderator', () => {
    setupComponent('USER', 99);
    cy.get('.post-footer .btn-edit').should('not.exist');
    cy.get('.post-footer .btn-delete').should('not.exist');
  });

  it('ar trebui să afișeze butoanele de Edit/Delete pe întrebare dacă ești AUTORUL', () => {
    setupComponent('USER', 1);
    cy.get('.post-footer .btn-edit').should('exist');
    cy.get('.post-footer .btn-delete').should('exist');
    cy.get('.answer-footer .delete').should('not.exist');
  });

  it('ar trebui să afișeze butoanele de Edit/Delete PESTE TOT dacă ești MODERATOR', () => {
    setupComponent('MODERATOR', 99);
    cy.get('.post-footer .btn-edit').should('exist');
    cy.get('.post-footer .btn-delete').should('exist');
    cy.get('.answer-footer .btn-link').contains('Edit').should('exist');
    cy.get('.answer-footer .delete').should('exist');
  });

  it('ar trebui să prindă evenimentul de vot', () => {
    setupComponent();

    //verificam consola direct, fara aliasuri, pentru a evita erorile TypeScript
    cy.window().then((win) => {
      cy.spy(win.console, 'log');
    });

    cy.get('.post-footer .vote-btn.up').click();

    //verificarea gasirii textului corect
    cy.window().then((win) => {
      expect(win.console.log).to.have.been.calledWith('Vot up pentru question');
    });
  });

});
