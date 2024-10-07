const auth = {
  isAuthenticated() {
    return !!localStorage.getItem('username'); 
  },
  
  login() {
    localStorage.setItem('authenticated', true);
  },

  logout() {
    localStorage.removeItem('username');
    localStorage.removeItem('authenticated');
    localStorage.removeItem('clientId');
  },

  setUsername(username) {
    localStorage.setItem('username', username); 
  },

  setClientId(clientId) {
    localStorage.setItem('clientId', clientId); 
  },

  getAuthenticatedUser() {
    return localStorage.getItem('username'); 
  },

  getClientId() {
    return localStorage.getItem('clientId');
  },
};

export default auth;
