# Rippling Mini — Take-Home Demo

A mini version of Rippling's core feature: add an employee, and Gmail, Slack, and Payroll get provisioned automatically.

Built for the Rippling SWE interview.

---

## What it does

1. You fill out a form with an employee's name, email, department, and role
2. Hit submit — the backend creates the employee and instantly spins up 3 app accounts
3. You see the employee in a table with Gmail / Slack / Payroll badges
4. Click any employee to open a modal — mark each app as done, see the audit log

That's it. Simple, but it shows the core Rippling idea: one action triggers a workflow.

---

## Tech

- **Backend** — Spring Boot 3, Java 17, H2 in-memory DB, JPA
- **Frontend** — React + Vite, plain CSS, Axios

---

## Run it (2 commands)

**Terminal 1 — Backend**
```bash
cd rippling-demo
.\mvnw.cmd spring-boot:run
```

**Terminal 2 — Frontend**
```bash
cd frontend
npm run dev
```

Open http://localhost:5173

The app starts with 5 demo employees already loaded. No setup, no config.

---

## Endpoints (if you want to poke around)

| Method | URL | What it does |
|--------|-----|--------------|
| POST | /api/employees | Add employee + auto-provision |
| GET | /api/employees | List all employees |
| GET | /api/provisions/employee/:id | Get app provisions for employee |
| POST | /api/provisions/:id/complete | Mark a provision done |
| GET | /api/provisions/logs/employee/:id | Get audit log |

H2 console → http://localhost:8080/h2-console
JDBC URL: `jdbc:h2:mem:ripplingdb` · Username: `sa` · Password: *(blank)*