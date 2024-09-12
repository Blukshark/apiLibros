const auth = {
    isAuthenticated: () => {
        return localStorage.getItem('isAuthenticated') === 'true';
    },
    login: () => {
        localStorage.setItem('isAuthenticated', 'true');
    },
    logout: () => {
        localStorage.removeItem('isAuthenticated');
    },
};
export default auth;