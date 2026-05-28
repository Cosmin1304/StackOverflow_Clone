describe('Editarea propriei intrebari', () => {

  const timestamp = Date.now();

  //date pentru test
  const user = {
    username: `autor_edit_${timestamp}`,
    email: `edit_${timestamp}@test.com`,
    password: 'ParolaCorecta123!',
    phone: '+40700000000'
  };

  const titluInitial = `Intrebare originala ${timestamp}`;
  const continutInitial = `Acesta este continutul care urmeaza sa fie modificat.`;

  const titluModificat = `Intrebare modificata ${timestamp}`;
  const continutModificat = `Am editat cu succes aceasta postare din Cypress!`;

  it('ar trebui sa permita autorului sa isi editeze propria postare si sa salveze modificarile', () => {

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


    //datele intrebarii
    cy.url().should('eq', Cypress.config().baseUrl + '/');
    cy.get('.btn-ask').click();

    cy.get('#title').type(titluInitial);
    cy.get('#body').type(continutInitial);

    cy.intercept('POST', '**/api/topics').as('postQuestion');
    cy.get('.ask-card .btn-submit').click();
    cy.wait('@postQuestion');


    //navigarea la intrebare
    cy.url().should('eq', Cypress.config().baseUrl + '/');

    cy.intercept('GET', '**/api/topics/*').as('getTopic');

    cy.contains('.question-card .title', titluInitial).click();

    //prinde request
    cy.wait('@getTopic');

    cy.get('body').should('contain.text', titluInitial);

    cy.get('.btn-edit').should('be.visible').click();


    //modificarea datelor si salvarea
    cy.get('#title').should('be.visible').clear().type(titluModificat);
    cy.get('#body').clear().type(continutModificat);


    cy.intercept('PUT', '**/api/topics/*').as('updateQuestion');

    cy.contains('button', 'Salvează').click();

    //așteptam ca backend-ul sa proceseze modificarea si sa intoarca 200 OK
    cy.wait('@updateQuestion').its('response.statusCode').should('eq', 200);


    //verificare interfata
    //validarea ca interfata s-a actualizat cu noile date, evitand erorile de tip TypeError
    cy.get('body').should('contain.text', titluModificat);
    cy.get('body').should('contain.text', continutModificat);

    //validare titlul vechi s-a schimbat
    cy.get('body').should('not.contain.text', titluInitial);
  });
});
