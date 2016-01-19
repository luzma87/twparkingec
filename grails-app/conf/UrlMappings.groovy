class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller: 'inicio', action: 'index')
        "403"(controller: 'errores', action: 'forbidden')
        "404"(controller: 'errores', action: 'notFound')
        "500"(controller: 'errores', action: 'serverError')
	}
}
