describe('Securitate CRUD - Editare și Ștergere', () => {

  const timestamp = Date.now();

  //genereaza date unice pentru cei doi actori
  const userAutor = { username: `autor_${timestamp}`, email: `a_${timestamp}@test.com`, password: 'ParolaCorecta123!', phone: '+40700000001' };
  const userStrain = { username: `strain_${timestamp}`, email: `b_${timestamp}@test.com`, password: 'ParolaCorecta123!', phone: '+40700000002' };

  const titluIntrebare = `Întrebare securitate ${timestamp}`;

  it('nu ar trebui sa arate butoanele de editare/stergere pentru un utilizator care nu este autorul', () => {

    //autorul inrebarii
    cy.visit('/register');
    cy.get('input[name="username"]').type(userAutor.username);
    cy.get('input[name="email"]').type(userAutor.email);
    cy.get('input[name="phoneNumber"]').type(userAutor.phone);
    cy.get('input[name="password"]').type(userAutor.password);
    cy.get('.btn-submit').click();

    cy.visit('/login');
    cy.get('#username').type(userAutor.username);
    cy.get('#password').type(userAutor.password);
    cy.get('.btn-submit').click();

    //autorul posteaza intrebarea
    cy.get('.btn-ask').click();
    cy.get('#title').type(titluIntrebare);
    cy.get('#body').type('Dacă nu sunt autorul, ar trebui să pot șterge asta?');

    cy.intercept('POST', '**/api/topics').as('postQuestion');
    cy.get('.ask-card .btn-submit').click();
    cy.wait('@postQuestion');

    //ne delogam din contul autorului
    cy.get('.navbar').contains('Log out').click();

    //celalalt utilizator
    cy.visit('/register');
    cy.get('input[name="username"]').type(userStrain.username);
    cy.get('input[name="email"]').type(userStrain.email);
    cy.get('input[name="phoneNumber"]').type(userStrain.phone);
    cy.get('input[name="password"]').type(userStrain.password);
    cy.get('.btn-submit').click();

    cy.visit('/login');
    cy.get('#username').type(userStrain.username);
    cy.get('#password').type(userStrain.password);
    cy.get('.btn-submit').click();

    //validare ui
    //aștepta ca Angular sa termine procesul de logare si sa mute pe home
    cy.url().should('eq', Cypress.config().baseUrl + '/');

    //cauta intrebarea pe pagina (fara refresh)
    cy.contains('.question-card .title', titluIntrebare).click();

    //asteapta sa se randeze pagina complet
    cy.get('.question-header h2', { timeout: 10000 }).should('contain', titluIntrebare);

    //verifica ca elementele de modificare lipsesc complet din DOM
    cy.get('.clasa-edit').should('not.exist');
    cy.get('.clasa-delete').should('not.exist');
  });
});
