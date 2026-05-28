describe('Flux - Adăugare Comentariu / Răspuns', () => {

  const timestamp = Date.now();

  const user = {
    username: `comentator_${timestamp}`,
    email: `comentator_${timestamp}@test.com`,
    password: 'ParolaCorecta123!',
    phone: '+40700000000'
  };

  const titluIntrebare = `Întrebare pentru comentariu ${timestamp}`;
  const textComentariu = `Acesta este un comentariu de test generat de Cypress la ${timestamp}.`;

  it('ar trebui să permita unui utilizator sa adauge un comentariu si sa il afiseze instant', () => {

    //creare utilizator
    cy.visit('/register');
    cy.get('input[name="username"]').type(user.username);
    cy.get('input[name="email"]').type(user.email);
    cy.get('input[name="phoneNumber"]').type(user.phone);
    cy.get('input[name="password"]').type(user.password);
    cy.get('.btn-submit').click();

    cy.url().should('include', '/login');
    cy.get('#username').type(user.username);
    cy.get('#password').type(user.password);
    cy.get('.btn-submit').click();

    //postarea unei intrebari
    cy.url().should('eq', Cypress.config().baseUrl + '/');
    cy.get('.btn-ask').click();

    cy.get('#title').type(titluIntrebare);
    cy.get('#body').type('Avem nevoie de un loc unde să postăm comentariul.');

    cy.intercept('POST', '**/api/topics').as('postQuestion');
    cy.get('.ask-card .btn-submit').click();
    cy.wait('@postQuestion');

    //navigarea la intrebare
    cy.url().should('eq', Cypress.config().baseUrl + '/');

    //seteaza interceptarile inainte de click
    cy.intercept('GET', '**/api/topics/*').as('getTopic');
    cy.intercept('GET', '**/api/answers/topic/*').as('getAnswers');

    cy.contains('.question-card .title', titluIntrebare).click();

    //asteapta sa apara intrebarea si lista de raspunsuri
    cy.wait('@getTopic');
    cy.wait('@getAnswers');

    cy.get('body').should('contain.text', titluIntrebare);

    //adaugarea comentariului
    cy.get('textarea').should('be.visible').type(textComentariu);
    cy.intercept('POST', '**/api/answers/topic/*').as('postComment');
    cy.get('.btn-submit').contains(/postează/i).click();
    cy.wait('@postComment').its('response.statusCode').should('eq', 200);

    //merge pe pagina principala si inapoi pe intrebare pt ca cypress nu stie ca trebuie sa deseneze din nou
    cy.get('.navbar').contains('StackOverflow').click();
    cy.url().should('eq', Cypress.config().baseUrl + '/');
    cy.intercept('GET', '**/api/topics/*').as('getTopicFresh');
    cy.intercept('GET', '**/api/answers/topic/*').as('getAnswersFresh');
    cy.contains('.question-card .title', titluIntrebare).click();

    //asteapta dupa baza de date
    cy.wait('@getTopicFresh');
    cy.wait('@getAnswersFresh');

    //verifica actualizarea, ca textul a fost schimbat
    cy.get('body', { timeout: 10000 }).should('contain.text', textComentariu);
  });
});
