package bankActionServlets;

import BankSystem.BankSystem;
import DTOs.BankSystemDTO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import userManager.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "RewindYazServlet",urlPatterns = {"/RewindYaz"})
public class RewindYazServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        BankSystem bankEngine = ServletUtils.getBankSystem(getServletContext());
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        BankSystemDTO bankSystemDTO = null;
        synchronized (getServletContext()) {
            userManager.addVersionToBankSystemVersionMap(bankEngine.getBankSystemDTO());
            Integer rewindYaz = bankEngine.rewindYaz();
            bankSystemDTO = userManager.getBankSystemDTOByYaz(rewindYaz);
            bankSystemDTO.setRewind(true);

            response.setStatus(HttpServletResponse.SC_OK);
            //create the response json string
            Gson gson = new Gson();

            String jsonResponse = gson.toJson(bankSystemDTO);
            try (PrintWriter out = response.getWriter()) {
                out.print(jsonResponse);
                out.flush();
            }
        }
    }
}

