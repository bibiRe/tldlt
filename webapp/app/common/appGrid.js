/*
 * 统一表格
 * hhe 2015.9.16
 */
Ext.ns("Ext.ux.app.grid");
Ext.ux.app.grid.AppGridObj = function(createColumnModelFunc, createStoreFunc,
		createGridFunc) {	
	this.columnModel = createColumnModelFunc();
	this.store = createStoreFunc();
	this.gridBottomBar = new Ext.ux.grid.BottomBar(this.store);
	this.grid = createGridFunc(this.store, this.columnModel,
			this.gridBottomBar.bbar);
	this.store.load({
		params: {
			start: 0,
			limit: this.gridBottomBar.getPageSize(),
			firstload: 'true'
		}
	});
};

Ext.ux.app.grid.AppParamGridObj = function(createColumnModelFunc, createStoreFunc,
		createGridFunc, param) {	
	this.columnModel = createColumnModelFunc(param);
	this.store = createStoreFunc(param);
	this.gridBottomBar = new Ext.ux.grid.BottomBar(this.store);
	this.grid = createGridFunc(this.store, this.columnModel,
			this.gridBottomBar.bbar, param);
	this.store.load({
		params: {
			start: 0,
			limit: this.gridBottomBar.getPageSize(),
			firstload: 'true'
		}
	});
};