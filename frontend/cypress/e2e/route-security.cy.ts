it('ar trebui să redirecționeze utilizatorii nelogați către login', () => {
  const protectedRoutes = ['/ask', '/profile', '/moderator'];

  protectedRoutes.forEach(route => {
    cy.visit(route);
    //verifica ca am fost trimisi la login
    cy.url().should('include', '/login');
  });
});
