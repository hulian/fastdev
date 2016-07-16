package com.fastdev.rapidoid.server;

import org.rapidoid.http.Req;
import org.rapidoid.http.ReqRespHandler;
import org.rapidoid.http.Resp;
import org.rapidoid.setup.On;
import com.fastdev.core.config.Config;
import com.fastdev.core.config.Server;
import com.fastdev.core.dispatcher.Dispatcher;
import com.fastdev.core.server.ServerProvider;

public class RapidoidServerImpl implements ServerProvider{


	private Dispatcher dispatcher;

	@Override
	public void start( Config config) {
		On.req(new ReqRespHandler() {
			
			private static final long serialVersionUID = -4328223652407360220L;

			@Override
			public Object execute(Req req, Resp resp) throws Exception {
				return dispatcher.dispatch(req.posted());
			}
		});
	}

	@Override
	public void stop() {
	}


	@Override
	public void setDispatcher(final Dispatcher dispatcher) {
		this.dispatcher=dispatcher;
	}

	@Override
	public Server getServerName() {
		return Server.RAPIDOID;
	}
}
