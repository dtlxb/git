package action;

import java.util.List;

import model.Book;
import model.Posto;
import service.AppService;

public class PostoAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	
	private AppService appService;

	public void setAppService(AppService appService) {
		this.appService = appService;
	}

	@Override
	public String execute() throws Exception {
	 
		List<Posto> postos = appService.AdmingetAllPostos();
		request().setAttribute("postos", postos);

		return SUCCESS;
	}
}
