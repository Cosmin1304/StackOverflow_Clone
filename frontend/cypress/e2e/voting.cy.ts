describe('Sistem de votare si reputatie', () => {

  const timestamp = Date.now();
  const autor = { username: `autor_${timestamp}`, email: `a_${timestamp}@ppth.com`, password: 'ParolaCorecta123!', phone: '+40711111111' };
  const votant = { username: `votant_${timestamp}`, email: `v_${timestamp}@ppth.com`, password: 'ParolaCorecta123!', phone: '+40722222222' };
  const titluIntrebare = `Intrebarea E2E ${timestamp}`;

  it('ar trebui să penalizeze votantul cu -1.5 la downvote', () => {

    //partea ce tine de autor
    cy.visit('/register');
    cy.get('input[name="username"]').type(autor.username);
    cy.get('input[name="email"]').type(autor.email);
    cy.get('input[name="phoneNumber"]').type(autor.phone);
    cy.get('input[name="password"]').type(autor.password);
    cy.get('.btn-submit').click();

    cy.url().should('include', '/login');
    cy.get('#username').type(autor.username);
    cy.get('#password').type(autor.password);
    cy.get('.btn-submit').click();

    //crearea intrebari
    cy.url().should('eq', Cypress.config().baseUrl + '/');
    cy.get('.btn-ask').click();
    cy.get('#title').should('be.visible').type(titluIntrebare);
    cy.get('#body').type('Am nevoie de o părere.');

    cy.intercept('POST', '**/api/topics').as('postQuestion');
    cy.get('.ask-card .btn-submit').click();
    cy.wait('@postQuestion');

    //autorul intra pe intrebare
    cy.url().should('eq', Cypress.config().baseUrl + '/');
    cy.intercept('GET', '**/api/topics/*').as('getTopic');
    cy.intercept('GET', '**/api/answers/topic/*').as('getAnswers');
    cy.contains('.question-card .title', titluIntrebare).click();
    cy.wait('@getTopic');
    cy.wait('@getAnswers');

    //folosim .blur() pentru validarea Angular
    cy.get('textarea').should('be.visible').type('Acesta este răspunsul meu.').blur();

    cy.intercept('POST', '**/api/answers/topic/*').as('postAnswer');
    cy.get('.btn-submit').contains(/postează/i).click();
    cy.wait('@postAnswer').its('response.statusCode').should('eq', 200);

    //da timp bazei de date sa salveze tranzactia
    cy.wait(1500);

    cy.get('.navbar').contains('StackOverflow').click();
    cy.url().should('eq', Cypress.config().baseUrl + '/');

    //intra pe intrebare pentru a da refresh si vedea raspunsul
    cy.intercept('GET', '**/api/topics/*').as('getTopicFresh');
    cy.intercept('GET', '**/api/answers/topic/*').as('getAnswersFresh');
    cy.contains('.question-card .title', titluIntrebare).click();
    cy.wait('@getTopicFresh');
    cy.wait('@getAnswersFresh');

    //validarea raspunsului
    cy.get('body', { timeout: 10000 }).should('contain.text', 'Acesta este răspunsul meu.');

    cy.get('.navbar').contains('Log out').click();


    //se da votul
    cy.visit('/register');
    cy.get('input[name="username"]').type(votant.username);
    cy.get('input[name="email"]').type(votant.email);
    cy.get('input[name="phoneNumber"]').type(votant.phone);
    cy.get('input[name="password"]').type(votant.password);
    cy.get('.btn-submit').click();

    cy.url().should('include', '/login');
    cy.get('#username').type(votant.username);
    cy.get('#password').type(votant.password);
    cy.get('.btn-submit').click();

    cy.url().should('eq', Cypress.config().baseUrl + '/');

    cy.intercept('GET', '**/api/topics/*').as('getTopicVotant');
    cy.intercept('GET', '**/api/answers/topic/*').as('getAnswersVotant');
    cy.contains('.question-card .title', titluIntrebare).click();
    cy.wait('@getTopicVotant');
    cy.wait('@getAnswersVotant');

    //verifica iar ca textul sa existe si dea downvote
    cy.get('body', { timeout: 10000 }).should('contain.text', 'Acesta este răspunsul meu.');

    //seteaza interceptile pentru cererea de votare
    cy.intercept('POST', '**/api/answers/*/vote*').as('voteRequest');
    cy.get('.vote-btn.down').last().should('be.visible').click();
    cy.wait('@voteRequest').its('response.statusCode').should('eq', 200);


    //verifica scor = -1.5
    //merge pe profil
    cy.get('.navbar').contains('Log out').click();
    cy.url().should('include', '/login');

    //votantul se logheaza din nou
    cy.get('#username').type(votant.username);
    cy.get('#password').type(votant.password);
    cy.get('.btn-submit').click();
    cy.get('.navbar').contains('Profil').click();
    cy.url().should('include', '/profile');

    //verifica scorul
    cy.get('.stats-box strong', { timeout: 10000 }).should('contain.text', '-1.5');
  });
});
