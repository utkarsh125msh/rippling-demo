import { useState, useEffect } from 'react'
import { createEmployee, getAllEmployees } from './api'
import ProvisionModal from './ProvisionModal'
import './index.css'

const DEPARTMENTS = ['Engineering', 'Marketing', 'Sales', 'Finance', 'HR', 'Design', 'Operations']
const ROLES = ['Software Engineer', 'Product Manager', 'Designer', 'Analyst', 'Manager', 'Director', 'VP']

const emptyForm = { firstName: '', lastName: '', email: '', department: '', role: '' }

export default function App() {
  const [employees, setEmployees]     = useState([])
  const [form, setForm]               = useState(emptyForm)
  const [loading, setLoading]         = useState(false)
  const [error, setError]             = useState('')
  const [success, setSuccess]         = useState('')
  const [selected, setSelected]       = useState(null)

  useEffect(() => { fetchEmployees() }, [])

  const fetchEmployees = async () => {
    try {
      const res = await getAllEmployees()
      setEmployees(res.data)
    } catch (e) {
      console.error('Failed to fetch employees', e)
    }
  }

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value })
    setError('')
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!form.firstName || !form.lastName || !form.email || !form.department || !form.role) {
      setError('All fields are required.')
      return
    }
    setLoading(true)
    setError('')
    setSuccess('')
    try {
      await createEmployee(form)
      setSuccess(`✓ ${form.firstName} ${form.lastName} added — Gmail, Slack & Payroll provisioned.`)
      setForm(emptyForm)
      fetchEmployees()
    } catch (e) {
      setError(e.response?.data || 'Something went wrong.')
    } finally {
      setLoading(false)
    }
  }

  // Dashboard counts
  const totalProvisions = employees.reduce((acc, e) => acc + (e.appProvisions?.length || 0), 0)
  const doneProvisions  = employees.reduce((acc, e) => acc + (e.appProvisions?.filter(p => p.status === 'DONE').length || 0), 0)
  const pendingCount    = totalProvisions - doneProvisions

  return (
    <div className="app">
      <header className="header">
        <div className="header-inner">
          <div className="logo">
            <span className="logo-mark">R</span>
            <span className="logo-text">Rippling <span className="logo-sub">mini</span></span>
          </div>
          <p className="header-tagline">Add employee → Auto-provision everything</p>
        </div>
      </header>

      <main className="main">

        {/* Dashboard Tiles */}
        <div className="dashboard">
          <div className="tile">
            <div className="tile-value">{employees.length}</div>
            <div className="tile-label">Total Employees</div>
          </div>
          <div className="tile">
            <div className="tile-value">{totalProvisions}</div>
            <div className="tile-label">Apps Provisioned</div>
          </div>
          <div className="tile tile-green">
            <div className="tile-value">{doneProvisions}</div>
            <div className="tile-label">Completed</div>
          </div>
          <div className="tile tile-yellow">
            <div className="tile-value">{pendingCount}</div>
            <div className="tile-label">Pending</div>
          </div>
        </div>

        {/* Add Employee Form */}
        <section className="card form-card">
          <h2 className="card-title">Add New Employee</h2>
          <p className="card-subtitle">Creates account + auto-provisions Gmail · Slack · Payroll</p>

          <form onSubmit={handleSubmit} className="form">
            <div className="form-row">
              <div className="field">
                <label>First Name</label>
                <input name="firstName" value={form.firstName} onChange={handleChange} placeholder="Jane" autoComplete="off" />
              </div>
              <div className="field">
                <label>Last Name</label>
                <input name="lastName" value={form.lastName} onChange={handleChange} placeholder="Doe" autoComplete="off" />
              </div>
            </div>

            <div className="field">
              <label>Work Email</label>
              <input name="email" type="email" value={form.email} onChange={handleChange} placeholder="jane.doe@yourcompany.com" autoComplete="off" />
            </div>

            <div className="form-row">
              <div className="field">
                <label>Department</label>
                <select name="department" value={form.department} onChange={handleChange}>
                  <option value="">Select...</option>
                  {DEPARTMENTS.map(d => <option key={d}>{d}</option>)}
                </select>
              </div>
              <div className="field">
                <label>Role</label>
                <select name="role" value={form.role} onChange={handleChange}>
                  <option value="">Select...</option>
                  {ROLES.map(r => <option key={r}>{r}</option>)}
                </select>
              </div>
            </div>

            {error   && <div className="alert alert-error">{error}</div>}
            {success && <div className="alert alert-success">{success}</div>}

            <button type="submit" className="btn-primary" disabled={loading}>
              {loading ? 'Provisioning...' : '+ Add Employee & Provision Apps'}
            </button>
          </form>
        </section>

        {/* Employee Table */}
        <section className="card table-card">
          <div className="table-header">
            <h2 className="card-title">Employees</h2>
            <span className="badge-count">{employees.length} total</span>
          </div>

          {employees.length === 0 ? (
            <div className="empty-state">
              <p>No employees yet. Add one above to see auto-provisioning in action.</p>
            </div>
          ) : (
            <div className="table-wrap">
              <table className="table">
                <thead>
                  <tr>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Department</th>
                    <th>Role</th>
                    <th>Status</th>
                    <th>Apps</th>
                    <th></th>
                  </tr>
                </thead>
                <tbody>
                  {employees.map(emp => (
                    <tr key={emp.id} className="tr-clickable" onClick={() => setSelected(emp)}>
                      <td className="td-name">{emp.firstName} {emp.lastName}</td>
                      <td className="td-email">{emp.email}</td>
                      <td>{emp.department}</td>
                      <td>{emp.role}</td>
                      <td>
                        <span className={`status-badge status-${emp.status.toLowerCase()}`}>
                          {emp.status}
                        </span>
                      </td>
                      <td>
                        <div className="app-pills">
                          {emp.appProvisions?.map(p => (
                            <span key={p.id} className={`app-pill pill-${p.status.toLowerCase()}`} title={p.provisionedAccount}>
                              {p.appName}
                            </span>
                          ))}
                        </div>
                      </td>
                      <td>
                        <button className="btn-view" onClick={e => { e.stopPropagation(); setSelected(emp) }}>
                          View →
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </section>
      </main>

      {/* Provision Modal */}
      {selected && (
        <ProvisionModal
          employee={selected}
          onClose={() => setSelected(null)}
          onUpdate={fetchEmployees}
        />
      )}
    </div>
  )
}