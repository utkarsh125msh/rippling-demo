import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: { 'Content-Type': 'application/json' }
})

export const createEmployee = (data) => api.post('/employees', data)
export const getAllEmployees = () => api.get('/employees')
export const getEmployee = (id) => api.get(`/employees/${id}`)
export const getProvisions = (employeeId) => api.get(`/provisions/employee/${employeeId}`)
export const completeProvision = (id) => api.post(`/provisions/${id}/complete`)
export const getLogs = (employeeId) => api.get(`/provisions/logs/employee/${employeeId}`)