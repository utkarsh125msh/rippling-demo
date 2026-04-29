import { useState, useEffect } from 'react'
import { getProvisions, getLogs, completeProvision } from './api'

const APP_ICONS = {
  Gmail:   '✉️',
  Slack:   '💬',
  Payroll: '💰'
}

const APP_COLORS = {
  Gmail:   '#ea4335',
  Slack:   '#4a154b',
  Payroll: '#0ea5e9'
}

export default function ProvisionModal({ employee, onClose, onUpdate }) {
  const [provisions, setProvisions] = useState([])
  const [logs, setLogs]             = useState([])
  const [completing, setCompleting] = useState(null)

  useEffect(() => {
    fetchData()
  }, [employee.id])

  // Close on Escape key
  useEffect(() => {
    const handler = (e) => { if (e.key === 'Escape') onClose() }
    window.addEventListener('keydown', handler)
    return () => window.removeEventListener('keydown', handler)
  }, [])

  const fetchData = async () => {
    const [pRes, lRes] = await Promise.all([
      getProvisions(employee.id),
      getLogs(employee.id)
    ])
    setProvisions(pRes.data)
    setLogs(lRes.data)
  }

  const handleComplete = async (provisionId) => {
    setCompleting(provisionId)
    try {
      await completeProvision(provisionId)
      await fetchData()
      onUpdate()
    } finally {
      setCompleting(null)
    }
  }

  const allDone = provisions.length > 0 && provisions.every(p => p.status === 'DONE')

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className="modal" onClick={e => e.stopPropagation()}>

        {/* Modal Header */}
        <div className="modal-header">
          <div className="modal-avatar">
            {employee.firstName[0]}{employee.lastName[0]}
          </div>
          <div className="modal-title-block">
            <h2 className="modal-name">{employee.firstName} {employee.lastName}</h2>
            <p className="modal-meta">{employee.role} · {employee.department}</p>
            <p className="modal-meta">{employee.email}</p>
          </div>
          {allDone && (
            <span className="fully-provisioned-badge">✓ Fully Provisioned</span>
          )}
          <button className="modal-close" onClick={onClose}>✕</button>
        </div>

        {/* App Provisions */}
        <div className="modal-section">
          <h3 className="modal-section-title">App Provisioning</h3>
          <div className="provision-list">
            {provisions.map(p => (
              <div key={p.id} className={`provision-row ${p.status === 'DONE' ? 'provision-done' : ''}`}>
                <div className="provision-app-icon" style={{ background: APP_COLORS[p.appName] + '18', color: APP_COLORS[p.appName] }}>
                  {APP_ICONS[p.appName] || '📦'}
                </div>
                <div className="provision-info">
                  <div className="provision-app-name">{p.appName}</div>
                  <div className="provision-account">{p.provisionedAccount}</div>
                  {p.completedAt && (
                    <div className="provision-completed-at">
                      Completed {new Date(p.completedAt).toLocaleString()}
                    </div>
                  )}
                </div>
                <div className="provision-right">
                  <span className={`status-badge status-${p.status.toLowerCase()}`}>
                    {p.status === 'DONE' ? '✓ Done' : p.status}
                  </span>
                  {p.status !== 'DONE' && (
                    <button
                      className="btn-complete"
                      disabled={completing === p.id}
                      onClick={() => handleComplete(p.id)}
                    >
                      {completing === p.id ? 'Marking...' : 'Mark Done'}
                    </button>
                  )}
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Workflow Audit Log */}
        <div className="modal-section">
          <h3 className="modal-section-title">Workflow Audit Log</h3>
          <div className="log-list">
            {logs.map(log => (
              <div key={log.id} className="log-row">
                <span className={`log-dot log-dot-${log.eventType.toLowerCase()}`} />
                <div className="log-content">
                  <span className="log-message">{log.message}</span>
                  <span className="log-time">
                    {new Date(log.timestamp).toLocaleString()}
                  </span>
                </div>
              </div>
            ))}
          </div>
        </div>

      </div>
    </div>
  )
}