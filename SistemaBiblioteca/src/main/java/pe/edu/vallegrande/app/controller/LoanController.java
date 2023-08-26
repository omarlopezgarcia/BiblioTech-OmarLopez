package pe.edu.vallegrande.app.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import pe.edu.vallegrande.app.model.Loan;
import pe.edu.vallegrande.app.service.CrudLoanService;

@WebServlet({ "/LoanBuscar", "/LoanProcesar", "/LoanActualizar", "/LoanHistorial" })
public class LoanController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private CrudLoanService service = new CrudLoanService();

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getServletPath();
		switch (path) {
		case "/LoanBuscar":
			buscar(request, response);
			break;
		case "/LoanProcesar":
			procesar(request, response);
			break;
		case "/LoanActualizar":
			actualizar(request, response);
			break;
		case "/LoanHistorial":
			historial(request, response);
			break;
		}
	}

	private void procesar(HttpServletRequest request, HttpServletResponse response) {
		String accion = request.getParameter("accion");
		Loan bean = new Loan();
		bean.setIdentifier(Integer.parseInt(request.getParameter("identifier")));
		bean.setUser_identifier(request.getParameter("user_identifier"));
		bean.setBook_identifier(request.getParameter("book_identifier"));
		bean.setAmount(request.getParameter("amount"));
		bean.setReturn_date(request.getParameter("return_date"));
		try {
			switch (accion) {
			case ControllerUtil.CRUD_NUEVO:
				service.insert(bean);
				break;
			case ControllerUtil.CRUD_EDITAR:
				service.update(bean);
				break;
			case ControllerUtil.CRUD_ELIMINAR:
				service.delete(bean);
				break;
			case ControllerUtil.CRUD_RESTAURAR:
				service.restore(bean.getIdentifier().toString());
				break;
			case ControllerUtil.CRUD_ELIMINATE:
				service.eliminate(bean.getIdentifier().toString());
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + accion);
			}
			ControllerUtil.responseJson(response, "Proceso ok.");
		} catch (Exception e) {
			ControllerUtil.responseJson(response, e.getMessage());
		}
	}

	private void buscar(HttpServletRequest request, HttpServletResponse response) {
		String user = request.getParameter("user");
		String book = request.getParameter("book");
		Loan bean = new Loan();
		bean.setUser_identifier(user);
		bean.setBook_identifier(book);
		List<Loan> lista = service.get(bean);
		Gson gson = new Gson();
		String data = gson.toJson(lista);
		ControllerUtil.responseJson(response, data);
	}

	private void actualizar(HttpServletRequest request, HttpServletResponse response) {
		List<Loan> lista = service.getActive();
		Gson gson = new Gson();
		String data = gson.toJson(lista);
		ControllerUtil.responseJson(response, data);
	}

	private void historial(HttpServletRequest request, HttpServletResponse response) {
		List<Loan> lista = service.getInactive();
		Gson gson = new Gson();
		String data = gson.toJson(lista);
		ControllerUtil.responseJson(response, data);
	}

}
